/*
package restSecrurity.controllers;

import config.ApplicationConfig;
import config.HibernateConfig;
import config.Routes;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import restSecrurity.persistance.Role;
import restSecrurity.persistance.User;
import utills.Populator;

import java.time.LocalDate;
import java.time.LocalTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class DudeControllerTest {


    private static EntityManagerFactory emf;
    private static User userStudent, userAdmin,userInstructor;
    private static Role student, admin,instructor;
    private static Object adminToken;
    private static Object studentToken;
    private static Object instructorToken;

    @BeforeAll
    static void setUpAll(){
        emf = HibernateConfig.getEntityManagerFactoryConfig(true);
        //emf = HibernateConfig.getEntityManagerFactoryConfig();

        RestAssured.baseURI = "http://localhost:7777/api";
        ApplicationConfig applicationConfig = ApplicationConfig.getInstance();
        applicationConfig.initiateServer()
                .startServer(7777)
                .setExceptionOverallHandling()
                .setRoute(Routes.setRoutes(true))
                .checkSecurityRoles();

        student = new Role("STUDENT");
        admin = new Role("ADMIN");
        instructor = new Role("INSTRUCTOR");

        userStudent = new User("test@student.com","test",student);
        userAdmin = new User("test@admin.com","test",admin);
        userInstructor = new User("test@instructor.com","test",instructor);

        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(student);
            em.persist(admin);
            em.persist(instructor);
            em.persist(userStudent);
            em.persist(userAdmin);
            em.persist(userInstructor);
            em.getTransaction().commit();
        }
        adminToken = getToken(userAdmin.getUsername(), "test");
        studentToken = getToken(userStudent.getUsername(), "test");
        instructorToken = getToken(userInstructor.getUsername(),"test");

    }



    @AfterAll
    static void tearDown(){
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("delete from Role").executeUpdate();
            em.createQuery("delete from User").executeUpdate();
            em.getTransaction().commit();
        }
    }

    public static Object getToken(String email, String password)
    {
        return login(email, password);
    }

    private static Object login(String email, String password)
    {
        String json = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", email, password);

        var token = given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("/security/auth/login")
                .then()
                .extract()
                .response()
                .body()
                .path("token");

        return "Bearer " + token;
    }



    @BeforeEach
    void setUp() {

        Populator.populateDatabase(true);

        */
/*
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();

            //em.createQuery("delete from users").executeUpdate();
            em.createQuery("delete from Location").executeUpdate();
            em.createQuery("delete from EventSpec").executeUpdate();
            em.createQuery("delete from Event").executeUpdate();
            em.createQuery("delete from Zipcode").executeUpdate();

            em.createNativeQuery("ALTER SEQUENCE event_id_seq RESTART WITH 1").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE location_id_seq RESTART WITH 1").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE eventspec_id_seq RESTART WITH 1").executeUpdate();

            Event event1 = new Event("testEvent","test med test på",99999,"pictureUrl");
            Location location1 = new Location("Fredensvej 19");
            EventSpec eventSpec1 = new EventSpec(LocalDate.now(), LocalTime.now(),30,"Den vilde","denVilde@hej.com", Status.UPCOMING,30, Category.EVENT);
            Zipcode zipcode1 = new Zipcode(2970,"Hørsholm");

            Event event2 = new Event("testEvent2","test med test på",99999,"pictureUrl");
            Location location2 = new Location("Thyrasvej 4");
            EventSpec eventSpec2 = new EventSpec(LocalDate.now(), LocalTime.now(),30,"Den vilde","denVilde@hej.com", Status.UPCOMING,30,Category.WORKSHOP);
            //Zipcode zipcode2 = new Zipcode(2970,"Hørsholm");

            em.persist(zipcode1);

            location1.addZipcode(zipcode1);
            location1.setEventSpec(eventSpec1);

            location2.addZipcode(zipcode1);
            location2.setEventSpec(eventSpec2);

            event1.addLocation(location1);
            event2.addLocation(location2);

            em.persist(event2);

            event1.addUser(userStudent);

            em.persist(event1);
            em.getTransaction().commit();
            em.clear();
            //User found = em.find(User.class,user124.getEmail());
        }
        *//*

    }
*/
/*
    @Test
    void getAll() {
        RestAssured.given()
                .header("Authorization", adminToken)
                .when()
                .get("/memoryStorage")
                .then().log().all()
                .body("dudeList.size()",equalTo(5));
    }*//*


    @Test
    void getById() {
    }
}*/
