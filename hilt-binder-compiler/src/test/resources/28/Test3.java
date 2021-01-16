import com.paulrybitskyi.hiltbinder.BindType;
import com.paulrybitskyi.hiltbinder.keys.MapLongKey;

@MapLongKey(3L)
@BindType(contributesTo = BindType.Collection.MAP)
public class Test3 implements Testable {}