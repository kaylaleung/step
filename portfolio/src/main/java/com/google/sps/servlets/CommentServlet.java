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
   * Id param for retrieving comment for matching blog post id
   */
  private static final String ID_PARAM = "id";

  /** 
   * Time param for the time of submission of a comment. Used
   * to sort comments in most recently submitted order 
   */
  private static final String TIME_PARAM = "timestamp";

  /** 
   * CurID param for the unique page id that comments will be posted to 
   */
  private static final String CURID_PARAM = "current-id";

  /** 
   * Comment param specifies the entity class queried for 
   */
  private static final String COMMENT = "Comment";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    Query query = new Query(COMMENT).addSort(TIME_PARAM, SortDirection.DESCENDING);
    String requestStr = request.getParameter(ID_PARAM);

    if (requestStr != null) {
      Query.FilterPredicate filter = new Query.FilterPredicate(ID_PARAM, FilterOperator.EQUAL, requestStr);
      query.setFilter(filter).addSort(TIME_PARAM, SortDirection.DESCENDING);
    }

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    List<Comment> comments = new ArrayList<>();

    for (Entity entity : results.asIterable()) {
      String name = (String) entity.getProperty(NAME_PARAM);
      String id = (String) entity.getProperty(ID_PARAM);
      String text = (String) entity.getProperty(TEXT_PARAM);
      long timestamp = (long) entity.getProperty(TIME_PARAM);
      Comment comment = new Comment(name, id, text, timestamp);
      comments.add(comment);
    }

    String json = convertToJson(comments);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    String id = isNullOrEmpty(request.getParameter(CURID_PARAM));
    String name = isNullOrEmpty(request.getParameter(NAME_PARAM));
    String text = isNullOrEmpty(request.getParameter(TEXTIN_PARAM));
    long timestamp = System.currentTimeMillis();

    Entity commentEntity = new Entity(COMMENT);
    commentEntity.setProperty(NAME_PARAM, name);
    commentEntity.setProperty(ID_PARAM, id);
    commentEntity.setProperty(TEXT_PARAM, text);
    commentEntity.setProperty(TIME_PARAM, timestamp);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    response.setContentType("text/html;");
    response.sendRedirect("/blog.html?id=" + id);
  }

  private String isNullOrEmpty(String param) {
    if (param == null || param.equals("")) {
      throw new NullPointerException("Null or empty parameter returned");
    }
    return param;
  }

  private String convertToJson(List<Comment> comments) {
      Gson gson = new Gson();
      return gson.toJson(comments);
  }
}
