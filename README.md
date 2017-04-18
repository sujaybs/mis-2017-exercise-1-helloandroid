# mis-2017-exercise-1-helloandroid

This project is an Android application, which implements the functionality of HTTP(S) client.
It is compatible with Android API levels 15-25 and uses
[Retrolambda](https://github.com/orfjackal/retrolambda) as a means of support for Java 8 features.

## UI

The user interface consists of
a [Button](https://developer.android.com/reference/android/widget/Button.html),
an [EditText](https://developer.android.com/reference/android/widget/EditText.html)
and a [Crosswalk project's](https://crosswalk-project.org/)
[XWalkView](https://crosswalk-project.org/apis/embeddingapidocs/reference/org/xwalk/core/XWalkView.html).

After the Button is clicked (or the ```IME_ACTION_DONE``` event is triggered from the keypad)
the contents of the EditText are validated and interpreted as a URL.
An HTTPS GET request is then done to fetch the underlying web content,
which is to be interpreted and displayed through
[XWalkView](https://crosswalk-project.org/apis/embeddingapidocs/reference/org/xwalk/core/XWalkView.html).

## HTTP(S) client implementation

For network operations [Volley](https://github.com/google/volley) library was used.

StringRequests are performed asynchronously,
through the usage of Volley's
[RequestQueue](https://github.com/google/volley/blob/master/src/main/java/com/android/volley/RequestQueue.java):

```
volleyRequestQueue.add(
                new StringRequest(
                        url,
                        this::handleResponse,
                        this::handleError
                )
        );
```

## HTML interpretation and display

A third party WebView-like module
([XWalkView](https://crosswalk-project.org/apis/embeddingapidocs/reference/org/xwalk/core/XWalkView.html))
is used to provide a feature-rich web content interpretation.
Benefits of the selected module include stable and improved interpretation of
CSS, HTML and JavaScript (JS) across the broad span of Android API levels.

However, as the functionality of the app is limited to one network request,
complex web apps may not be displayed perfectly,
as additional JS and/or CSS files may be linked to the interpreted HTML
and required for precise display of the content.

## Robustness and exception handling

For improved robustness, URL format is checked through the usage of regular expression (`Patterns.WEB_URL`,
in accordance to RFC 3987).

Through the usage of native Uri class, the URL is parsed and scheme is substituted with HTTPS
for security against web traffic capture.

[ConnectivityManager](https://developer.android.com/reference/android/net/ConnectivityManager.html)
is used to check network availability before trying to perform network requests.

Network exceptions are handled through Volley's
[TimeoutError](https://github.com/google/volley/blob/ddfb86659df59e7293df9277da216d73c34aa800/src/main/java/com/android/volley/TimeoutError.java),
[ServerError](https://github.com/google/volley/blob/ddfb86659df59e7293df9277da216d73c34aa800/src/main/java/com/android/volley/ServerError.java),
[AuthFailureError](https://github.com/google/volley/blob/ddfb86659df59e7293df9277da216d73c34aa800/src/main/java/com/android/volley/AuthFailureError.java),
[NetworkError](https://github.com/google/volley/blob/ddfb86659df59e7293df9277da216d73c34aa800/src/main/java/com/android/volley/NetworkError.java)
and
[ParseError](https://github.com/google/volley/blob/ddfb86659df59e7293df9277da216d73c34aa800/src/main/java/com/android/volley/ParseError.java).