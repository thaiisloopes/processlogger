package com.semanticweb.processlogger.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({"classpath*:beans.xml"})
public class XmlConfig {
}
