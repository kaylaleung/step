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

import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private static final String TEXT_PARAM = "text";
  private static final String TEXTIN_PARAM = "text-input";
  private static final String NAME_PARAM = "name";
  private static final String TIME_PARAM = "timestamp";
  private static final String COMMENT = "Comment";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    Query query = new Query(COMMENT).addSort(TIME_PARAM, SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    List<String> comments = new ArrayList<>();

    for (Entity entity : results.asIterable()) {
      String text = (String) entity.getProperty(TEXT_PARAM);
      comments.add(text);
    }

    String json = convertToJson(comments);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    final String text = request.getParameter(TEXTIN_PARAM);
    final String name = request.getParameter(NAME_PARAM);
    long timestamp = System.currentTimeMillis();

    Entity taskEntity = new Entity(COMMENT);
    taskEntity.setProperty(TEXT_PARAM, text);
    taskEntity.setProperty(TIME_PARAM, timestamp);
    taskEntity.setProperty(NAME_PARAM, name);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(taskEntity);

    response.setContentType("text/html;");
    response.sendRedirect("blog.html");
  }

  private String convertToJson(List<String> comments) {
      Gson gson = new Gson();
      return gson.toJson(comments);
  }
}
