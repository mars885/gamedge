import com.paulrybitskyi.hiltbinder.BindType;

@TestMapKey(TestMapKey.Type.ONE)
@BindType(contributesTo = BindType.Collection.MAP)
public class Test1 implements Testable {}