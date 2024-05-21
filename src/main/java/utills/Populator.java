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

    public static void populateDatabase(boolean isTest)  {
            EntityManager em = HibernateConfig.getEntityManagerFactoryConfig(isTest).createEntityManager();
            em.getTransaction().begin();

            try {
                // Initialize some users
                User user1 = new User();
                user1.setUsername("patrick@user1.com");
                user1.setPassword("password123");
                Set<Role> roles = new HashSet<>();
                roles.add(new Role("user"));
                user1.setRoles(roles);
                em.persist(user1);

                // Initialize some categories
                Category category1 = new Category();
                category1.setName("General Discussion");
                em.persist(category1);

                // Initialize some threads
                Thread thread1 = new Thread();
                thread1.setTitle("Welcome to the forum");
                thread1.setCreatedDate(LocalDateTime.now());
                thread1.setUser(user1);
                thread1.setCategory(category1);
                em.persist(thread1);

                Thread thread2 = new Thread();
                thread2.setTitle("hvaa hEr Er FeDt Ik?");
                thread2.setCreatedDate(LocalDateTime.now());
                thread2.setUser(user1);
                thread2.setCategory(category1);
                em.persist(thread2);

                Thread thread3 = new Thread();
                thread3.setTitle("NEEEJ!");
                thread3.setCreatedDate(LocalDateTime.now());
                thread3.setUser(user1);
                thread3.setCategory(category1);
                em.persist(thread3);

                // Initialize some posts
                Post post1 = new Post();
                post1.setContent("Hello everyone!");
                post1.setCreatedDate(LocalDateTime.now());
                post1.setUser(user1);
                post1.setThread(thread1);
                em.persist(post1);

                Post post2 = new Post();
                post2.setContent("Hello everyone!x2");
                post2.setCreatedDate(LocalDateTime.now());
                post2.setUser(user1);
                post2.setThread(thread2);
                em.persist(post2);

                // Initialize a reply as a new thread of posts
                Post replyPost = new Post();
                replyPost.setContent("Welcome, John!");
                replyPost.setCreatedDate(LocalDateTime.now());
                replyPost.setUser(user1);
                replyPost.setThread(thread1);

                Reply reply = new Reply();
                reply.setParentPost(post1);
                reply.setPosts(new HashSet<>());
                reply.getPosts().add(replyPost);
                replyPost.setReply(reply);

                em.persist(reply);
                em.persist(replyPost);

                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
                e.printStackTrace();
            }
        }
    }
