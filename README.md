# Android example of Dependency Injection for testing with Repository pattern  

The goal of this app is to show a simple exception of a home-made dependency injection pattern that uses repositories to ease testing.  
The example being used is of a login screen.  

## Injectors

Let's start with the injectors.  
  
An injector is a class that knows how to instantiate an object, obtaining all required dependencies to do so.  
This is helpful for us because we want our views (`Activity`, `Fragment`) to be as dumb as possible, so they have no business knowing about stuff like our repositories that are used to talk with our API.  

How does an injector look like then?

```java
public class PresenterInjector {

    private final CommonInjector commonInjector;
    private final RepoInjector repoInjector;

    public PresenterInjector(CommonInjector commonInjector, RepoInjector repoInjector) {
        this.commonInjector = commonInjector;
        this.repoInjector = repoInjector;
    }

    public LoginPresenter createLoginPresenter(LoginView loginView) {
        return new LoginPresenter(
                loginView,
                commonInjector.getLocalStorage(),
                repoInjector.getUsersRepository());
    }

}
```

As we can see, the `createLoginPresenter` method only receives a reference to a `LoginView` interface, and gets the rest of the dependencies from elsewhere. The `LoginPresenter` constructor now looks something like this:

```java
    public LoginPresenter(
            LoginView loginView,
            LocalStorage localStorage,
            UsersRepository usersRepository) {
        super(loginView);
        this.localStorage =  localStorage;
        this.usersRepository = usersRepository;
    }
```

That's great! As we can see, we receive those dependencies as a parameter, so we have no need to know how to create them. If we now want to test this presenter, we just have to provided mocked `LocalStorage` and `UsersRepository` classes.  
  
Lets take a look at how we used to do this:

```java
    public LoginPresenter(LoginView loginView) {
        super(loginView);
        this.localStorage = LocalStorage.getInstance();
        this.usersRepository = RetrofitServices.users();
    }
```

There are a few problems here:
* `LocalStorage` uses `SharedPreferences` to store data. This is not available in a testing environment, so it will likely crash.
* `UsersRepository` uses our network library, Retrofit. We don't want to make an actual API call while testing!
* The presenter has knowledge of our networking library. This doesn't look like a deal-breaker, but we are very tightly coupled with it. If for whatever reason we want to change it it will be a huge problem and will envolve modifying a lot of classes, especially in big projects.  
  
If we wanted to overcome this issues in other ways, we could add `setters` for this variables to use in testing. The problem is that they will be available in non-testing code as well, and be mistakenly used. Also, some classes such as the retrofit one depend on being initialized. So, in order for this to work, we would have to initialize the whole networking library (which we won't be using), which may be slow and take time in our tests! That doesn't sound right, does it?

You may be wondering where the view gets the injector from. Our `Application` class has them! It implements some simple interfaces such as `PresenterInjectorSupplier` that have a single method to retrieve these injectors. They are used all over the app, and can be retrieved with some convenience methods on `WoloxActivity` or `WoloxFragment`:

```java
	protected CommonInjector getCommonInjector() {
        return ((CommonInjectorSupplier) getApplication()).getCommonInjector();
    }

    protected RepoInjector getRepoInjector() {
        return ((RepoInjectorSupplier) getApplication()).getRepoInjector();
    }

    protected PresenterInjector getPresenterInjector() {
        return ((PresenterInjectorSupplier) getApplication()).getPresenterInjector();
    }
```

And in our `Application` class:

```java
    private CommonInjector commonInjector;
    private PresenterInjector presenterInjector;
    private RepoInjector repoInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitServices.init();
        commonInjector = new CommonInjector(this);
        repoInjector = new RepoInjector();
        presenterInjector = new PresenterInjector(commonInjector, repoInjector);
    }
```


## Tests

Now lets take a look at how our tests look!
```java
    @Mock LoginView loginView;
    @Mock LocalStorage localStorage;
    @Mock UsersRepository usersRepository;

    private LoginPresenter loginPresenter;
    private User exampleUser;

//    Not anymore!
//    @BeforeClass
//    public static void initServices() {
//        RetrofitServices.init();
//    }

    @Before
    public void initPresenter() {
        loginPresenter = new LoginPresenter(loginView, localStorage, usersRepository);
    }
```
As we can see, all dependencies can be easily mocked and passed as parameters to the presenter. Also, there is no need to initialize the networking library anymore, saving precious time when running on our tests with a CI tool.  
We also have no dependencies on `Retrofit` classes on our tests, since we are using the Repository pattern! (Sorry, I kinda skipped over it).  

Let's compare both approaches:  
  
Before:
```java
	@Mock Call<Void> loginUserCall;

    @Test
    public void checkUserLoggedSuccessfully() {
        inputValidCredentials();
        doAnswer(invocationOnMock -> {
            invocationOnMock.getArgumentAt(0, WoloxCallback.class).onSuccess(exampleUser);
            return null;
        }).when(loginUserCall).enqueue(any(WoloxCallback.class));
        when(usersService.login(any(UserRequest.class)))
            .thenReturn(loginUserCall);

        loginPresenter.loginPressed();

        verify(localStorage).storeInSharedPreferences(Configuration.KEY_EMAIL, exampleUser);
        verify(loginView).goToMainScreen();
    }
```

After:
```java
    @Test
    public void checkUserLoggedSuccessfully() {
        inputValidCredentials();
        doAnswer(invocationOnMock -> {
            invocationOnMock.getArgumentAt(1, RepoCallback.class).onSuccess(exampleUser);
            return null;
        }).when(usersRepository).login(any(UserRequest.class), any(RepoCallback.class));

        loginPresenter.loginPressed();

        verify(localStorage).storeInSharedPreferences(Configuration.KEY_EMAIL, exampleUser);
        verify(loginView).goToMainScreen();
    }
```

On the first case, we have to use a retrofit `Call`, mock it and call `enqueue` on it. We have a test that is following the steps required by the networking library. This doesn't look right, it makes our code very tightly coupled with Retrofit and makes the code unnecessarily longer and more complex.

## Conclusion
This is not perfect, there are many flaws that can be pointed out, but it's a good first step to start testing on Android and in the process get used to dependency injection, so that using [Dagger 2](http://google.github.io/dagger/), a framework that simplifies this process, doesn't seem like magic.













