package com.sosa.infraccionservice.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;


@EnableWs
@Configuration
public class WebServiceConfig  extends WsConfigurerAdapter{

	
	@Bean
	public ServletRegistrationBean<MessageDispatcherServlet>
			messageDispatcherServlet(ApplicationContext applicationContext){
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean<>(servlet, "/ws/*");
	}
	@Bean
	public XsdSchema asticulosSchema() {
		return new SimpleXsdSchema(new ClassPathResource("infraccion-detalle.xsd"));
	}
	
	//localhost:8081/ws/productos.wsdl
	@Bean(name = "infraccion")
	public DefaultWsdl11Definition defaultWsdl11Definition2(XsdSchema articulosSchema) {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("infraccionPort");
		wsdl11Definition.setLocationUri("/ws");
		wsdl11Definition.setTargetNamespace("http://uss.com/infraccion-soap");
		wsdl11Definition.setSchema(articulosSchema);
		return wsdl11Definition;
	}
}

