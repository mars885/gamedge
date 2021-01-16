import com.paulrybitskyi.hiltbinder.BindType;
import com.paulrybitskyi.hiltbinder.keys.MapClassKey;

@MapClassKey(Test2.class)
@BindType(contributesTo = BindType.Collection.MAP)
public class Test2 implements Testable {}