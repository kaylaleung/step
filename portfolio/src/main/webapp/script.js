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

window.addEventListener('DOMContentLoaded', (event) => {

  var element = document.getElementsByClassName("menu")[0];

  element.addEventListener("click", toggleActiveSection);
  function toggleActiveSection(tab) {
      var elems = document.querySelector(".active");
      if (elems != null) {
        elems.classList.remove("active");
      }
      tab.target.classList.add("active");
      console.log("change active status");
      // console.log(elems);
      console.log(tab.target);

	}
});