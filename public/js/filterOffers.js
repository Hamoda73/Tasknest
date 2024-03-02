document
  .getElementById("searchForm")
  .addEventListener("submit", function (event) {
    event.preventDefault(); // Prevent default form submission

    // Get search criteria
    var entrepriseName = document.getElementById("entrepriseName").value;
    var domain = document.getElementById("domain").value;
    var post = document.getElementById("post").value;

    // Send Ajax request
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
      if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
        var results = JSON.parse(this.responseText);
        // Update UI with search results
        document.getElementById("searchResults").innerHTML =
          renderResults(results);
      }
    };
    xhr.open(
      "GET",
      "/search_offers?" +
        new URLSearchParams({ entrepriseName, domain, post }).toString()
    );
    xhr.send();
  });

function renderResults(results) {
  // Implement your logic to render search results here
  var html = "";
  results.forEach(function (result) {
    html +=
      "<div>" +
      result.entrepriseName +
      " - " +
      result.domain +
      " - " +
      result.post +
      "</div>";
  });
  return html;
}
