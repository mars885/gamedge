import com.paulrybitskyi.hiltbinder.BindType;
import com.paulrybitskyi.hiltbinder.keys.MapIntKey;

@MapIntKey(3)
@BindType(contributesTo = BindType.Collection.MAP)
public class Test3 implements Testable {}