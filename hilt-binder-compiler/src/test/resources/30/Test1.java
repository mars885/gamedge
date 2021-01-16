import com.paulrybitskyi.hiltbinder.BindType;
import com.paulrybitskyi.hiltbinder.keys.MapClassKey;

@MapClassKey(Test1.class)
@BindType(contributesTo = BindType.Collection.MAP)
public class Test1 implements Testable {}