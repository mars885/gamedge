import com.paulrybitskyi.hiltbinder.BindType;
import com.paulrybitskyi.hiltbinder.keys.MapClassKey;

@MapClassKey(Test3.class)
@BindType(contributesTo = BindType.Collection.MAP)
public class Test3 implements Testable {}