import com.paulrybitskyi.hiltbinder.BindType;
import com.paulrybitskyi.hiltbinder.keys.MapLongKey;

@MapLongKey(2L)
@BindType(contributesTo = BindType.Collection.MAP)
public class Test2 implements Testable {}