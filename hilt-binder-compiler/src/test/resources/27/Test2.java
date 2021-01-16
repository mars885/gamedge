import com.paulrybitskyi.hiltbinder.BindType;
import com.paulrybitskyi.hiltbinder.keys.MapIntKey;

@MapIntKey(2)
@BindType(contributesTo = BindType.Collection.MAP)
public class Test2 implements Testable {}