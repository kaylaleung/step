package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;	
import com.google.gson.Gson;
import com.google.sps.data.BlogPost;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/post")
public class PostServlet extends HttpServlet {

  /** 
   * Text param for setting and retrieving blog post text
   */
  private static final String POST_PARAM = "blogpost";

  /** 
   * Postin param for retrieving blog post from form input
   */
  private static final String POSTIN_PARAM = "blog-input";
  
  /** 
   * Title param for setting and retrieving the title of the blog post
   */
  private static final String TITLE_PARAM = "title";

   /** 
   * Time param for the time of submission of a blog post. Used
   * to sort blog post in most recently submitted order 
   */
  private static final String TIME_PARAM = "timestamp";

  /** 
   * Category param for the filtering blog post by category type 
   */
  private static final String CAT_PARAM = "category";

  /** 
   * Id param for blog posts. Used to determine which blog posts 
   * are displayed with their corresponding unique datastore id. 
   */
  private static final String ID_PARAM = "id";

  /** 
   * Blogpost param specifies the entity class queried for 
   */
  private static final String BLOGPOST = "BlogPost";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    Query query = new Query(BLOGPOST).addSort(TIME_PARAM, SortDirection.DESCENDING);
    String requestStr = request.getParameter(ID_PARAM);

    if (requestStr != null) {
      long requestId = Long.valueOf(requestStr);
      Key keyId = KeyFactory.createKey(BLOGPOST, requestId);
      Query.FilterPredicate filter = new Query.FilterPredicate("__key__", FilterOperator.EQUAL, keyId);
      query.setFilter(filter).addSort(TIME_PARAM, SortDirection.DESCENDING);
    }

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    List<BlogPost> posts = new ArrayList<>();

    for (Entity entity : results.asIterable()) {
      String title = (String) entity.getProperty(TITLE_PARAM);
      String id = Long.toString(entity.getKey().getId()); 
      String category = (String) entity.getProperty(CAT_PARAM);
      String blogpost = (String) entity.getProperty(POST_PARAM);
      long timestamp = (long) entity.getProperty(TIME_PARAM);
      BlogPost post = new BlogPost(title, id, category, blogpost, timestamp);
      posts.add(post);
    }

    String json = convertToJson(posts);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    String title = isNullOrEmpty(request.getParameter(TITLE_PARAM));
    String category = isNullOrEmpty(request.getParameter(CAT_PARAM));
    String blogpost = isNullOrEmpty(request.getParameter(POSTIN_PARAM));
    long timestamp = System.currentTimeMillis();

    Entity postEntity = new Entity(BLOGPOST);
    postEntity.setProperty(TITLE_PARAM, title);
    postEntity.setProperty(CAT_PARAM, category);
    postEntity.setProperty(POST_PARAM, blogpost);
    postEntity.setProperty(TIME_PARAM, timestamp);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(postEntity);

    response.setContentType("text/html;");
    response.sendRedirect("blog.html");
  }

  private String isNullOrEmpty(String param) {
    if (param == null || param.equals("")) {
      throw new NullPointerException("Null or empty parameter returned");
    }
    return param;
  }

  private String convertToJson(List<BlogPost> posts) {
      Gson gson = new Gson();
      return gson.toJson(posts);
  }
}
