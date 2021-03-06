package isel.leic.daw.hvac

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import isel.leic.daw.hvac.common.APPLICATION_TYPE
import isel.leic.daw.hvac.common.JSON_HOME_SUBTYPE
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class SampleInterceptor : HandlerInterceptor {
	private val logger = LoggerFactory.getLogger(SampleInterceptor::class.java)

	override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
		logger.info("preHandle() called")
		return true
	}
}

@Configuration
@EnableWebMvc
class ApiConfig : WebMvcConfigurer {

	override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
		val converter = converters.find {
			it is MappingJackson2HttpMessageConverter
		} as MappingJackson2HttpMessageConverter
		converter.objectMapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
		converter.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)

		val jsonHomeConverter = MappingJackson2HttpMessageConverter()
		jsonHomeConverter.objectMapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
		jsonHomeConverter.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
		jsonHomeConverter.supportedMediaTypes = listOf(MediaType(APPLICATION_TYPE, JSON_HOME_SUBTYPE))
		converters.add(jsonHomeConverter)
	}

	override fun addInterceptors(registry: InterceptorRegistry) {
		registry.addInterceptor(SampleInterceptor())
	}
}

@SpringBootApplication
class HvacSpringMvcApplication

fun main(args: Array<String>) {
	runApplication<HvacSpringMvcApplication>(*args)
}
