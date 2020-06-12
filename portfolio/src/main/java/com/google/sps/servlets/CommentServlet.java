// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;	
import com.google.gson.Gson;
import com.google.sps.data.Comment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/comment")
public class CommentServlet extends HttpServlet {

  /** 
   * Text param for setting and retrieving comment text
   */
  private static final String TEXT_PARAM = "text";

  /** 
   * Textin param for retrieving comment from form input
   */
  private static final String TEXTIN_PARAM = "text-input";

  /** 
   * Name param for setting and retrieving the name of the 
   * user submitting a comment
   */
  private static final String NAME_PARAM = "name";

  /** 
   * Time param for the time of submission of a comment. Used
   * to sort comments in most recently submitted order 
   */
  private static final String TIME_PARAM = "timestamp";
  
  /** 
   * Tag param for comments. Used to determine which comments are 
   * displayed with their  corresponding blog posts
   */
  private static final String TAG_PARAM = "tag";

  /** 
   * Url param for the page url that comments will be posted to and where the 
   * user will be redirected to after posting a comment. 
   */
  private static final String URL_PARAM = "current-url";

  /** 
   * Comment param specifies the entity class queried for 
   */
  private static final String COMMENT = "Comment";

  /** 
   * The path len param specifies length of the blog url path 
   * up until its tag specifier. 
   */
  private static final int PATH_LEN = 15;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String requestTag = request.getParameter(TAG_PARAM);
    List<Comment> comments = new ArrayList<>();

    if (requestTag != null) {
      Query.FilterPredicate filter = new Query.FilterPredicate(TAG_PARAM, FilterOperator.EQUAL, requestTag);
      Query query = new Query(COMMENT).setFilter(filter).addSort(TIME_PARAM, SortDirection.DESCENDING);
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      PreparedQuery results = datastore.prepare(query);

      for (Entity entity : results.asIterable()) {
        String tag = (String) entity.getProperty(TAG_PARAM);
        String name = (String) entity.getProperty(NAME_PARAM);
        String text = (String) entity.getProperty(TEXT_PARAM);
        long timestamp = (long) entity.getProperty(TIME_PARAM);
        Comment comment = new Comment(name, text, tag, timestamp);
        comments.add(comment);
      }
    }

    String json = convertToJson(comments);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String text = request.getParameter(TEXTIN_PARAM);
    String name = request.getParameter(NAME_PARAM);
    String url = request.getParameter(URL_PARAM);
    String tag = url.substring(PATH_LEN);
    long timestamp = System.currentTimeMillis();

    Entity commentEntity = new Entity(COMMENT);
    commentEntity.setProperty(TEXT_PARAM, text);
    commentEntity.setProperty(TAG_PARAM, tag);
    commentEntity.setProperty(TIME_PARAM, timestamp);
    commentEntity.setProperty(NAME_PARAM, name);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    response.setContentType("text/html;");
    response.sendRedirect(url);
  }

  private String convertToJson(List<Comment> comments) {
      Gson gson = new Gson();
      return gson.toJson(comments);
  }
}
