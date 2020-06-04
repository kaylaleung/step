package com.google.sps.servlets;

import com.google.sps.data.BlogPost;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.io.IOException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/post")
public class PostServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    Query query = new Query("BlogPost").addSort("timestamp", SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    ArrayList<BlogPost> posts = new ArrayList<>();

    for (Entity entity : results.asIterable()) {
      String title = (String) entity.getProperty("title");
      String blogpost = (String) entity.getProperty("blogpost");
      long timestamp = (long) entity.getProperty("timestamp");

      BlogPost post = new BlogPost(title, blogpost, timestamp);
      posts.add(post);
    }

    String json = convertToJson(posts);

    response.setContentType("application/json;");
    response.getWriter().println(json);

  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    String title = request.getParameter("title");
    String blogpost = request.getParameter("blog-input");
    long timestamp = System.currentTimeMillis();

    Entity postEntity = new Entity("BlogPost");
    postEntity.setProperty("title", title);
    postEntity.setProperty("blogpost", blogpost);
    postEntity.setProperty("timestamp", timestamp);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(postEntity);

    response.setContentType("text/html;");
    response.sendRedirect("blog.html");
  }

  private String convertToJson(ArrayList<BlogPost> posts) {
      Gson gson = new Gson();
      return gson.toJson(posts);
  }
} 
