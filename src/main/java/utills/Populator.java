package utills;

import config.HibernateConfig;
import jakarta.persistence.EntityManager;
import restSecrurity.DOA.memoryDAO.DudeMemoryDAO;

import restSecrurity.persistance.*;
import restSecrurity.persistance.Thread;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Populator {

    public static void populateDatabase(boolean isTest) {
        EntityManager em = HibernateConfig.getEntityManagerFactoryConfig(isTest).createEntityManager();
        em.getTransaction().begin();

        try {
            // Initialize roles
            Role userRole = new Role();
            Role adminRole = new Role();
            userRole.setName("user");
            adminRole.setName("admin");
            em.persist(userRole);
            em.persist(adminRole);

            // Initialize some users
            User user1 = new User();
            user1.setUsername("patrickUser2");
            user1.setPassword("1234");
            user1.setEmail("patrick@user2.com");
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            user1.setRoles(roles);
            em.persist(user1);

            // Initialize some categories
            Category category1 = new Category();
            category1.setName("General Discussion");
            Category category2 = new Category();
            category2.setName("Technical Support");

            em.persist(category1);
            em.persist(category2);

            // Create Threads
            Thread thread1 = new Thread("Welcome to the forum", "This is the first thread", user1, category1);
            Thread thread2 = new Thread("Need help with Java", "I'm stuck with a problem", user1, category2);

            em.persist(thread1);
            em.persist(thread2);

            // Create Posts
            Post post1 = new Post("This is a post in the first thread", user1, thread1);
            Post post2 = new Post("This is another post in the first thread", user1, thread1);
            Post post3 = new Post("This is a post in the second thread", user1, thread2);

            em.persist(post1);
            em.persist(post2);
            em.persist(post3);

            // Create Replies and associate with posts
            Reply reply1 = new Reply("This is a reply to the second post in the first thread", post2, user1);
            em.persist(reply1);
            post2.addReply(reply1); // Adding reply to post2
            em.persist(post2); // Updating post2 to include the new reply

            // Add posts to threads
            thread1.getPosts().add(post1);
            thread1.getPosts().add(post2);
            thread2.getPosts().add(post3);

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }


}
