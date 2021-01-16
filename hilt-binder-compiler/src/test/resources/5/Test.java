import com.paulrybitskyi.hiltbinder.BindType;

@BindType(to = Testable1.class)
public class Test implements Testable1, Testable2 {}