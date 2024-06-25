package com.sidorov.prac.lesson4.hw;


import com.sidorov.prac.lesson4.hw.entity.Post;
import com.sidorov.prac.lesson4.hw.entity.PostComment;
import com.sidorov.prac.lesson4.hw.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Homework {

    /**
     * Используя hibernate, создать таблицы:
     * 1. Post (публикация) (id, title)
     * 2. PostComment (комментарий к публикации) (id, text, post_id)
     * <p>
     * Написать стандартные CRUD-методы: создание, загрузка, удаление. - загрузка это чтение? просто не совсем понятно, обычно под зогрузкой имеется в виду загрузка куда либо, а тут после создания идет и немного сбивает с толку
     * <p>
     * Доп. задания:
     * 1. * В сущностях post и postComment добавить поля timestamp с датами.
     * 2. * Создать таблицу users(id, name) и в сущностях post и postComment добавить ссылку на юзера.
     * 3. * Реализовать методы:
     * 3.1 Загрузить все комментарии публикации
     * 3.2 Загрузить все публикации по идентификатору юзера
     * 3.3 ** Загрузить все комментарии по идентификатору юзера
     * 3.4 **** По идентификатору юзера загрузить юзеров, под чьими публикациями он оставлял комменты.
     * // userId -> List<User>
     * <p>
     * <p>
     * Замечание:
     * 1. Можно использовать ЛЮБУЮ базу данных (например, h2)
     * 2. Если запутаетесь, приходите в группу в телеграме или пишите мне @inchestnov в личку.
     */

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");

        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();

            addUser("Умный_юзер3000",session);
            addUser("Очень_умный_юзер",session);
            addUser("Гениальный_юзер",session);
            addUser("Вау_насколько_он_умен",session);

            User user1 = getUserById(1L, session);
            User user2 = getUserById(2L, session);
            User user3 = getUserById(3L, session);
            User user4 = getUserById(4L, session);

            addPost(user1,"Этот пост о важных вещах", session);
            addPost(user2,"Этот пост о очень важных вещах", session);
            addPost(user3,"Этот пост действительно о важных вещах", session);
            addPost(user4,"Этот пост действительно о  очнь важных вещах", session);
            addPost(user1,"Бесполезный пост", session);

            Post post1 = findPostById(1L, session);
            Post post2 = findPostById(2L, session);
            Post post3 = findPostById(3L, session);
            Post post4 = findPostById(4L, session);
            Post post5 = findPostById(5L, session);

            addCommentToPost(user1,post2, "важный пост", session);
            addCommentToPost(user1,post2, "важный пост", session);
            addCommentToPost(user2,post1, "хороший пост", session);
            addCommentToPost(user3,post1, "хороший пост", session);
            addCommentToPost(user1,post3, "не зря потратил время читая этот пост", session);
            addCommentToPost(user2,post3, "не зря потратил время читая этот пост", session);
            addCommentToPost(user4,post3, "не зря потратил время читая этот пост", session);
            addCommentToPost(user1,post4, "вау", session);
            addCommentToPost(user3,post4, "вау", session);
            addCommentToPost(user2,post4, "вау", session);
            addCommentToPost(user4,post5, "фуу", session);
            addCommentToPost(user3,post5, "зря потратил время", session);
            addCommentToPost(user2,post5, "откуда это у меня в рекомендациях?", session);

            System.out.println("Все посты по юзеру " + getAllPostsByUser(user1, session));

            System.out.println("Все коментарии к публикации "+getAllCommentByPost(post1, session));

            System.out.println("Все коментарии от юзера "+getAllCommentByUser(user1,session));

            System.out.println("По идентификатору юзера загрузить юзеров, под чьими публикациями он оставлял комменты." +
                    getAllUsersByReactionOfAnotherUser(user1, session));

            //удаяляем бесполезный пост, наша соцсеть должна содержать только полезный контент
            deletePost(findPostById(5L, session), session);
            session.getTransaction().commit();
        }
    }

    private static List<User> getAllUsersByReactionOfAnotherUser(User user, Session session) {
        List<PostComment> comments = getAllCommentByUser(user, session);

        Set<Post> posts = new HashSet<>();
        for (PostComment comment : comments) {
            posts.add(comment.getPost());
        }

        Set<User> usersReacted = new HashSet<>();
        for (Post post : posts) {
            List<PostComment> postComments = getAllCommentByPost(post, session);
            for (PostComment postComment : postComments) {
                User commenter = postComment.getUser();
                if (!commenter.equals(user)) {
                    usersReacted.add(commenter);
                }
            }
        }

        return new ArrayList<>(usersReacted);
    }

    private static List<PostComment> getAllCommentByUser(User user, Session session){
        return session.createQuery("from PostComment where user= :user")
                .setParameter("user", user)
                .list();
    }

    private static List<PostComment> getAllCommentByPost(Post post, Session session) {
        return session.createQuery("from PostComment where post= :post")
                .setParameter("post", post)
                .list();
    }

    private static List<Post> getAllPostsByUser(User user, Session session){
        return session.createQuery("from Post where user= :user")
                .setParameter("user", user).list();
    }

    private static void addUser(String name, Session session){
        User user = new User();
        user.setName(name);
        session.persist(user);
    }

    private static User getUserById(Long id, Session session){
        return session.get(User.class, id);
    }

    private static void addPost(User user,String title, Session session) {
        Post post = new Post();
        post.setTitle(title);
        post.setUser(user);
        post.setTimestamp(LocalDateTime.now());


        session.persist(post);

    }

    private static Post findPostById(Long id, Session session) {
        return session.get(Post.class, id);
    }

    private static List<Post> findAllPosts(Session session) {
        return session.createQuery("from Post").list();
    }

    private static void deletePost(Post post, Session session) {
        deleteAllCommentFromPost(post, session);
        session.remove(post);
    }


    private static void addCommentToPost(User user ,Post post, String text, Session session) {
        PostComment comment = new PostComment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setText(text);
        comment.setTimestamp(LocalDateTime.now());
        session.persist(comment);
    }

    private static PostComment findCommentById(Long id, Session session) {
        return session.get(PostComment.class, id);
    }

    public static void deleteAllCommentFromPost(Post post, Session session) {
        session.createQuery("delete from PostComment where post = :post")
                .setParameter("post", post)
                .executeUpdate();
    }

    private static List<PostComment> findAllCommentByPost(Post post, Session session) {
        post = session.get(Post.class, post);//на счет вот этого действия я не уверен
        return post.getComments();
    }

    private static void deleteComment(PostComment comment, Session session) {
        session.remove(comment);
    }

}
