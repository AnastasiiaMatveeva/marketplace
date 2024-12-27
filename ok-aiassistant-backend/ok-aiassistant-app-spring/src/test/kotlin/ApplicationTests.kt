//package ru.otus.otuskotlin.aiassistant.app.spring
//
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import ru.otus.otuskotlin.aiassistant.app.spring.config.ModelConfigPostgres
//
//@SpringBootTest
//class ApplicationTests {
//    @Autowired
//    var pgConf: ModelConfigPostgres = ModelConfigPostgres()
//
//    @Test
//    fun contextLoads() {
//        assertEquals(5433, pgConf.psql.port)
//        assertEquals("test_db", pgConf.psql.database)
//    }
//}
