import com.paulrybitskyi.hiltbinder.BindType;
import com.paulrybitskyi.hiltbinder.keys.MapLongKey;

@MapLongKey(1L)
@BindType(contributesTo = BindType.Collection.MAP)
public class Test1 implements Testable {}