package testauto;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;

public class TestApi {
    String token;
    @BeforeClass
    public void testLoginUser(){
        Response hasil =
        RestAssured
                .given()
                .contentType("application/json")
                .body("""
                        { "email": "nikenriri05@gmail.com",
                          "password" : "150500Niken*"
                        }
                      """)
                .when()
                .post("https://api.rizqifauzan.com/api/auth/login");

        hasil.then().log().all();

        token = hasil.jsonPath().getString("data.token");
    }

    @Test
    public void testGetAllUser(){
        File jsonScema = new File("src/test/resources/jsonScema/getListUserScema.json");

        RestAssured
                .given()
                .auth()
                .oauth2(token)
                .when()
                .get("https://api.rizqifauzan.com/api/siswa")
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body(JsonSchemaValidator.matchesJsonSchema(jsonScema));
    }

    @Test
    public void testGetSpecificUser(){
        File jsonScema = new File("src/test/resources/jsonScema/getUserById.json");

        String userId = "24905351-4daf-4c50-ab00-f3e052f64e95";

        RestAssured
                .given()
                .auth()
                .oauth2(token)
                .when().get("https://api.rizqifauzan.com/api/siswa/" + userId)
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body(JsonSchemaValidator.matchesJsonSchema(jsonScema));
    }

    @Test
    public void testPostCeateUser(){
        String valueName = "Lulu Cantika";
        String valueNis = "124200015";
        String valueKelas = "XI-IPA-2";
        String valueJurusan = "IPA";
        String valueEmail = "cantika.15@gmail.com";
        String valueTelp = "08589044490";
        String valueAlamat = "Salatiga";

        JSONObject bodyObj = new JSONObject();

        bodyObj.put("nama", valueName);
        bodyObj.put("nis", valueNis);
        bodyObj.put("kelas", valueKelas);
        bodyObj.put("jurusan", valueJurusan);
        bodyObj.put("email", valueEmail);
        bodyObj.put("telepon", valueTelp);
        bodyObj.put("alamat", valueAlamat);

        RestAssured.given()
                .auth()
                .oauth2(token)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(bodyObj.toString())
                .when()
                .post("https://api.rizqifauzan.com/api/siswa")
                .then().log().all()
                .assertThat().statusCode(201)
                .assertThat().body("data.nama", Matchers.equalTo(valueName));
    }

    @Test
    public void testPutUpdateUser(){
        String valueName = "Widiany Putri Ramadhani";
        String valueNis = "124200090";
        String valueKelas = "XI-IPA-3";
        String valueJurusan = "IPA";
        String valueEmail = "widiany01@gmail.com";
        String valueTelp = "08123450534";
        String valueAlamat = "Jaksel";

        JSONObject bodyObj = new JSONObject();

        bodyObj.put("nama", valueName);
        bodyObj.put("nis", valueNis);
        bodyObj.put("kelas", valueKelas);
        bodyObj.put("jurusan", valueJurusan);
        bodyObj.put("email", valueEmail);
        bodyObj.put("telepon", valueTelp);
        bodyObj.put("alamat", valueAlamat);

        RestAssured.given()
                .auth()
                .oauth2(token)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(bodyObj.toString())
                .when()
                .put("https://api.rizqifauzan.com/api/siswa/75f923f0-d9c1-4de5-979d-f7f97736ce46")
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body("data.nama", Matchers.equalTo(valueName));
    }

    @Test
    public void testPatchUpdateUser(){
        String userId = "75f923f0-d9c1-4de5-979d-f7f97736ce46";
        String valueEmail = "widiany.putri@gmail.com";

        JSONObject bodyObj = new JSONObject();

        bodyObj.put("email", valueEmail);

        RestAssured.given()
                .auth()
                .oauth2(token)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(bodyObj.toString())
                .when()
                .patch("https://api.rizqifauzan.com/api/siswa/" + userId)
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body("data.email", Matchers.equalTo(valueEmail));
    }

    @Test
    public void testDeleteUser(){

        String userToDelete = "1eada7fe-4b0a-4dda-a25a-5ed9658ae0bb";

        RestAssured
                .given()
                .auth()
                .oauth2(token)
                .when()
                .delete("https://api.rizqifauzan.com/api/siswa/" + userToDelete)
                .then().log().all()
                .assertThat().statusCode(200);
    }

}
