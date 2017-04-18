# mis-2017-exercise-1-helloandroid

- This project creates a HTTP client android application consisting of a text box and a button. On pushing the button the contents of the text box are interpreted as URL. 
- Third party webview module called xWalkView module is used to provide a feature rich experience by using the latest API and also improve the performance of the CSS, HTML and JavaScript
- The checkURLAndLoad function checks the sanity of the URL and throws a MalformedURLException. Uri.parse is used to inform the content provider what is needed to access by reference. HTTPS is used for security to prevent exchanged information from being read in plain text. 
- Volley infrastructure is used to schedule all network requests and improve debugging. The exceptions handled are TimeoutError, ServerError, AuthFailureError, NetworkError and ParseError. 
- Connectivity Manager is used to monitor network changes and send broadcast intents when network connectivity changes. 

