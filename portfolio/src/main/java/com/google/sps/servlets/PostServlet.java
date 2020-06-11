package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;	
import com.google.gson.Gson;
import com.google.sps.data.BlogPost;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/post")
public class PostServlet extends HttpServlet {

  /** The post param is a string property identifier for setting and retrieving
   * the body of textual content stored for a blog post made by the user. */
  private static final String POST_PARAM = "blogpost";

  /** The postin param is a unique string identifier for retrieving the blog 
   * post text from a user through the form input in POST requests */
  private static final String POSTIN_PARAM = "blog-input";
  
  /** The title param is string property identifier for setting and retrieving
   * the title of the blog post created. If not included in a GET request, no 
   * blog title will be returned. */
  private static final String TITLE_PARAM = "title";

  /** The time param is a string property identifier for the time of submission 
   * upon a POST request for a submitted blog post. It is also the property 
   * used to sort posts in most recently submitted order */
  private static final String TIME_PARAM = "timestamp";

  /** The category pram is a string property identifier for the category of a 
   * blog post that will be retrieved via GET requests for filtering post by
   * category type. Many posts may have the same category */
  private static final String CAT_PARAM = "category";

  /** The tag param is a string property identifier for blog posts. It is the 
   * property used to determine which blog posts are displayed with their 
   * corresponding blog posts. If not included in a GET request, no blog posts 
   * will be returned. Tags for each post must be unique */
  private static final String TAG_PARAM = "tag";

  /** The blogpost param specifices the string identifier for the entity class 
   * that the GET request queries */
  private static final String BLOGPOST = "BlogPost";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    Query query;
    String requestTag = request.getParameter(TAG_PARAM);

    if (requestTag == null) {
      query = new Query(BLOGPOST).addSort(TIME_PARAM, SortDirection.DESCENDING);
    } 
    else {
      Query.FilterPredicate filter = new Query.FilterPredicate(TAG_PARAM, FilterOperator.EQUAL, requestTag);
      query = new Query(BLOGPOST).setFilter(filter).addSort(TIME_PARAM, SortDirection.DESCENDING);
    }
    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    List<BlogPost> posts = new ArrayList<>();

    for (Entity entity : results.asIterable()) {
      String tag = (String) entity.getProperty(TAG_PARAM);
      String title = (String) entity.getProperty(TITLE_PARAM);
      String category = (String) entity.getProperty(CAT_PARAM);
      String blogpost = (String) entity.getProperty(POST_PARAM);
      long timestamp = (long) entity.getProperty(TIME_PARAM);
      BlogPost post = new BlogPost(title, tag, category, blogpost, timestamp);
      posts.add(post);
    }

    String json = convertToJson(posts);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    String title = request.getParameter(TITLE_PARAM);
    String tag = request.getParameter(TAG_PARAM);
    String category = request.getParameter(CAT_PARAM);
    String blogpost = request.getParameter(POSTIN_PARAM);
    long timestamp = System.currentTimeMillis();

    Entity postEntity = new Entity(BLOGPOST);
    postEntity.setProperty(TITLE_PARAM, title);
    postEntity.setProperty(TAG_PARAM, tag);
    postEntity.setProperty(CAT_PARAM, category);
    postEntity.setProperty(POST_PARAM, blogpost);
    postEntity.setProperty(TIME_PARAM, timestamp);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(postEntity);

    response.setContentType("text/html;");
    response.sendRedirect("blog.html");
  }

  private String convertToJson(List<BlogPost> posts) {
      Gson gson = new Gson();
      return gson.toJson(posts);
  }
}
