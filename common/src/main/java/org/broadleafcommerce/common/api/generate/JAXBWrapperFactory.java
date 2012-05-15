/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.broadleafcommerce.common.api.generate;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.lang.reflect.Field;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCatchBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JTryBlock;
import com.sun.codemodel.JVar;
import org.apache.commons.beanutils.PropertyUtils;
import org.broadleafcommerce.common.api.APIUnwrapper;
import org.broadleafcommerce.common.api.APIWrapper;
import org.broadleafcommerce.common.api.BaseWrapper;
import org.springframework.context.ApplicationContext;

/**
 * Utilizes the jaxb sub-project for emitting java source files.
 * jaxb uses this subproject itself.
 *
 * @author Jeff Fischer
 */
public class JAXBWrapperFactory {

    public static void emitWrapper(Class<?> modelClass) throws Exception {
        String wrapperName = modelClass.getSimpleName();
        if (wrapperName.endsWith("Impl")) {
            wrapperName = wrapperName.substring(0, wrapperName.lastIndexOf("Impl"));
        }
        String rootElement = wrapperName.toLowerCase();
        wrapperName += "Wrapper";

        JCodeModel jCodeModel = new JCodeModel();
        JPackage jp = jCodeModel._package(modelClass.getPackage().getName());
        JDefinedClass jc = jp._class(wrapperName);

        if (!modelClass.isAnnotationPresent(RESTWrapper.class)) {
           throw new RuntimeException("A RESTWrapper annotation was not found on the passed in modelClass. Unable to emit a wrapper implementation.");
        }
        RESTWrapper restWrapper = modelClass.getAnnotation(RESTWrapper.class);

        Class<?> wrapperSuperClass = restWrapper.wrapperSuperClass();
        Class<?> modelInterface = restWrapper.modelInterface();
        jc._extends(wrapperSuperClass);
        jc._implements(jCodeModel.directClass(APIWrapper.class.getName()).narrow(modelInterface));
        jc._implements(jCodeModel.directClass(APIUnwrapper.class.getName()).narrow(modelInterface));

        //@XmlRootElement(name = "product")
        jc.annotate(XmlRootElement.class).param("name", rootElement);

        //@XmlAccessorType(value = XmlAccessType.FIELD)
        jc.annotate(XmlAccessorType.class).param("value", XmlAccessType.FIELD);

        //TODO Do something similar to emit the wrapper field for any collections or maps

        JMethod wrap = jc.method(JMod.PUBLIC, jc.owner().VOID, "wrap");
        wrap.annotate(Override.class);

        JClass runtimeException = jCodeModel.directClass(RuntimeException.class.getName());
        JTryBlock jWrapTryBlock = wrap.body()._try();
        JCatchBlock jWrapCatchBlock = jWrapTryBlock._catch(jCodeModel.directClass(Exception.class.getName()));
        JVar wrapException = jWrapCatchBlock.param("e");
        jWrapCatchBlock.body()._throw(JExpr._new(runtimeException).arg(wrapException));
        JBlock jWrapBlock = jWrapTryBlock.body();

        JVar model = wrap.param(modelInterface, "model");
        JVar request = wrap.param(HttpServletRequest.class, "request");
        JClass baseWrapper = jCodeModel.directClass(BaseWrapper.class.getName());
        JFieldRef implementationClass = JExpr.ref("implementationClass");
        jWrapBlock.assign(implementationClass, model.invoke("getClass").invoke("getName"));
        if (!wrapperSuperClass.equals(BaseWrapper.class)) {
            jWrapBlock.directStatement("super.wrap(model, request);");

            for (SuppressedElement suppressedElement : restWrapper.suppressedElements()) {
                jWrapBlock.directStatement("super." + suppressedElement.fieldName() + " = null;");
            }
        }

        JMethod unwrap = jc.method(JMod.PUBLIC, modelInterface, "unwrap");
        unwrap.annotate(Override.class);

        JTryBlock jUnwrapTryBlock = unwrap.body()._try();
        JCatchBlock jUnwrapCatchBlock = jUnwrapTryBlock._catch(jCodeModel.directClass(Exception.class.getName()));
        JVar unwrapException = jUnwrapCatchBlock.param("e");
        jUnwrapCatchBlock.body()._throw(JExpr._new(runtimeException).arg(unwrapException));
        JBlock jUnwrapBlock = jUnwrapTryBlock.body();

        unwrap.param(HttpServletRequest.class, "request");
        unwrap.param(ApplicationContext.class, "context");
        JVar unwrapped;
        if (!wrapperSuperClass.equals(BaseWrapper.class)) {
            unwrapped = jUnwrapBlock.decl(jCodeModel.ref(modelInterface), "unwrapped", JExpr._super().invoke(unwrap));
        } else {
            jUnwrapBlock.directStatement("if (implementationClass == null) throw new RuntimeException(\"Cannot unwrap object when implementationClass is null!\");");
            JClass clazz = jCodeModel.directClass(Class.class.getName());
            unwrapped = jUnwrapBlock.decl(jCodeModel.ref(modelInterface), "unwrapped", JExpr.cast(jCodeModel.ref(modelInterface), clazz.staticInvoke("forName").arg(implementationClass).invoke("newInstance")));
        }

        JClass propertyUtils = jCodeModel.directClass(PropertyUtils.class.getName());

        //declare the marked fields
        Field[] fields = modelClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(RESTElement.class)) {
                //@XmlElement
                JFieldVar myField = jc.field(JMod.PROTECTED, field.getType(), field.getName());
                myField.annotate(XmlElement.class);
                jWrapBlock.assign(myField, propertyUtils.staticInvoke("getProperty").arg(model).arg(field.getName()));
                jUnwrapBlock.staticInvoke(propertyUtils, "setProperty").arg(unwrapped).arg(field.getName()).arg(myField);

                /*
                TODO add code to support getting/setting for collections or maps for wrap and unwrap for collections and maps
                 */
            }
        }

        jUnwrapBlock._return(unwrapped);

        jCodeModel.build(new File("/Users/jfischer/Desktop"));
    }
}
