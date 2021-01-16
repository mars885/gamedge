import com.paulrybitskyi.hiltbinder.BindType;

@TestMapKey(TestMapKey.Type.TWO)
@BindType(contributesTo = BindType.Collection.MAP)
public class Test2 implements Testable {}