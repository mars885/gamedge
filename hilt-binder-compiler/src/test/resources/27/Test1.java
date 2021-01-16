import com.paulrybitskyi.hiltbinder.BindType;
import com.paulrybitskyi.hiltbinder.keys.MapIntKey;

@MapIntKey(1)
@BindType(contributesTo = BindType.Collection.MAP)
public class Test1 implements Testable {}