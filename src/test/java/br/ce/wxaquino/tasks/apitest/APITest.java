package br.ce.wxaquino.tasks.apitest;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import  io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class APITest {
	
	//executado antes da classe ser instanciada
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://localhost:8001/tasks-backend";
	}
	
	@Test
	public void deveRetornarTarefas() {
		/*
		forma de uso que tenta simular a linguagem gherkin que � utilizado no cucumber
		dado informa algumas pr�-condi��es; quando executar alguma determinada a��o; ent�o come�a a fazer as assertivas 
		organiza��o facilita a leitura
		esse teste n�o fazia sentido no contexto unit�rio
		*/
		RestAssured.given()
			.log().all() //loga a requisi��o
		.when()
			.get("/todo") //requisi��o get na rota, s� recurso
		.then()
		    .statusCode(200) //sucesso
			.log().all() //loga tudo que enviar
		;
	}
	
	@Test
	public void deveAdicionarTarefaComSucesso() {
		RestAssured.given()
			.body("{ \"task\": \"Teste via API\", \"dueDate\": \"2020-12-30\" }")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo") //a��o de salvar, padr�o API rest salva usando o m�todo POST
		.then()
			.statusCode(201) //recurso que pediu para inserir foi criado
		;
	}
	
	@Test
	public void naoDeveAdicionarTarefaInvalida() {
		RestAssured.given()
			.body("{ \"task\": \"Teste via API\", \"dueDate\": \"2010-12-30\" }")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo") //a��o de salvar, padr�o API rest salva usando o m�todo POST
		.then()
			.statusCode(400) //recurso que pediu para inserir foi criado
			.body("message", CoreMatchers.is("Due date must not be in past"))
		;
	}
}

