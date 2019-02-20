package article;

import database.DataBase;
import database.Records;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArticleControllerTest {

    private DataBase dataBase;

    @BeforeEach
    void beforeEach() {
        dataBase = new DataBase();
        dataBase.execute("delete from articles;");
    }


    @Nested
    class create {
        @Test
        void success() {
            //given
            //when
            String responseBody = new ArticleController().create(params("buy beer", "good beer"));
            //then
            Records records = dataBase.find("select * from articles;");
            assertEquals(1, records.size());
            assertEquals("buy beer", records.mapTo(Article.class).get(0).title);
            assertEquals(responseBody, "created");
            assertEquals(true, records.firstMapTo(Article.class).toJson().contains("good beer"), records.firstMapTo(Article.class).toJson());
        }

        @Test
        void tooLongTitle() {
            //given
            String responseBody = new ArticleController().create(params("buy beer xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx", "good beer"));
            //when
            Records records = dataBase.find("select * from articles;");
            //then
            assertEquals(0, records.size());
            assertEquals(responseBody, "invalid");
        }

        @Test
        void tooLongBody() {
            //given
            String responseBody = new ArticleController().create(params("buy beer", "good beer xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"));
            //when
            Records records = dataBase.find("select * from articles;");
            //then
            assertEquals(0, records.size());
            assertEquals(responseBody, "invalid");
        }
    }

    @Test
    void show() {
        //given
        new ArticleController().create(params("buy beer", "good beer"));
        Records records = dataBase.find("select * from articles;");
        //when
        Map<String, String> params= new HashMap<>();
        params.put("id", String.valueOf(records.first().get("id")));
        String responseBody = new ArticleController().show(params);
        //then
        assertEquals(true, responseBody.contains("title: buy beer"), responseBody);
    }

    @Test
    void index() {
        //given
        new ArticleController().create(params("buy beer", "good beer"));
        new ArticleController().create(params("buy wine", "good beer"));
        //when
        String responseBody = new ArticleController().index();
        //then
        assertEquals(true, responseBody.contains("title: buy beer"), responseBody);
        assertEquals(true, responseBody.contains("title: buy wine"), responseBody);
    }

    @Nested
    class search {
        @BeforeEach
        void beforeEach() {
            new ArticleController().create(params("buy beer", "good beer"));
            new ArticleController().create(params("buy wine", "good beer"));
            new ArticleController().create(params("buy chili wine", "bad beer"));
        }

        @Test
        void likeTitleAndBody() {
            //given
            Map<String, String> params = params("wine", "good");
            //when
            String responseBody = new ArticleController().search(params);
            //then
            assertEquals(false, responseBody.contains("title: buy beer"), responseBody);
            assertEquals(true, responseBody.contains("title: buy wine"), responseBody);
            assertEquals(false, responseBody.contains("title: buy chili wine"), responseBody);
        }

        @Test
        void likeTitle() {
            //given
            Map<String, String> params = params("wine", "");
            //when
            String responseBody = new ArticleController().search(params);
            //then
            assertEquals(true, responseBody.contains("title: buy wine"), responseBody);
            assertEquals(true, responseBody.contains("title: buy chili wine"), responseBody);
            assertEquals(false, responseBody.contains("title: buy beer"), responseBody);
        }

        @Test
        void likeBody() {
            //given
            Map<String, String> params = params("", "bad");
            //when
            String responseBody = new ArticleController().search(params);
            //then
            assertEquals(false, responseBody.contains("title: buy beer"), responseBody);
            assertEquals(false, responseBody.contains("title: buy wine"), responseBody);
            assertEquals(true, responseBody.contains("title: buy chili wine"), responseBody);
        }
    }


    private Map<String, String> params(String title, String body) {
        Map<String, String> map = new HashMap<>();
        map.put("title", title);
        map.put("body", body);
        return map;
    }

}
