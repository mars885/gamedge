import com.paulrybitskyi.hiltbinder.BindType;

@TestMapKey(TestMapKey.Type.THREE)
@BindType(contributesTo = BindType.Collection.MAP)
public class Test3 implements Testable {}