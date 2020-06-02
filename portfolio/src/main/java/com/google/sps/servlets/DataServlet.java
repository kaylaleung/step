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

import java.io.*; 
import com.google.sps.data.Comment;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.ArrayList;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private ArrayList<String> comments = new ArrayList<String>();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // Convert string of hard-coded messeges go JSON
    String json = convertToJson(comments);

    // Send JSON as response
    response.setContentType("application/json;");
    response.getWriter().println(json);

  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    String text = request.getParameter("text-input");
    String name = request.getParameter("name");

    long timestamp = System.currentTimeMillis();

    Entity taskEntity = new Entity("Comment");
    taskEntity.setProperty("text", text);
    taskEntity.setProperty("timestamp", timestamp);
    taskEntity.setProperty("name", name);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(taskEntity);

    response.setContentType("text/html;");
    response.sendRedirect("blog.html");
  }

  private String convertToJson(ArrayList<String> comments) {
      Gson gson = new Gson();
      return gson.toJson(comments);
  }

} 