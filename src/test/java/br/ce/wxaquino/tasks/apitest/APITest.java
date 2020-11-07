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
		forma de uso que tenta simular a linguagem gherkin que é utilizado no cucumber
		dado informa algumas pré-condições; quando executar alguma determinada ação; então começa a fazer as assertivas 
		organização facilita a leitura
		esse teste não fazia sentido no contexto unitário
		*/
		RestAssured.given()
			.log().all() //loga a requisição
		.when()
			.get("/todo") //requisição get na rota, só recurso
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
			.post("/todo") //ação de salvar, padrão API rest salva usando o método POST
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
			.post("/todo") //ação de salvar, padrão API rest salva usando o método POST
		.then()
			.statusCode(400) //recurso que pediu para inserir foi criado
			.body("message", CoreMatchers.is("Due date must not be in past"))
		;
	}
}

