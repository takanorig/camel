/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.beanclass;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.component.bean.BeanComponent;
import org.apache.camel.component.bean.BeanEndpoint;
import org.apache.camel.component.bean.BeanHolder;
import org.apache.camel.component.bean.ConstantBeanHolder;

/**
 * The <a href="http://camel.apache.org/class.html">Class Component</a> is for binding JavaBeans to Camel message exchanges based on class name.
 * <p/>
 * This component is an extension to the {@link org.apache.camel.component.bean.BeanComponent}.
 *
 * @version 
 */
public class ClassComponent extends BeanComponent {

    public ClassComponent() {
        super(ClassEndpoint.class);
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        ClassEndpoint endpoint = new ClassEndpoint(uri, this);
        endpoint.setBeanName(remaining);

        // bean name is the FQN
        String name = endpoint.getBeanName();
        Class<?> clazz = getCamelContext().getClassResolver().resolveMandatoryClass(name);
        // create bean
        Object bean = getCamelContext().getInjector().newInstance(clazz);

        // now set additional properties on it
        setProperties(bean, parameters);

        // and register the bean as a holder on the endpoint
        BeanHolder holder = new ConstantBeanHolder(bean, getCamelContext());
        endpoint.setBeanHolder(holder);

        return endpoint;
    }

}
