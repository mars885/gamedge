import com.paulrybitskyi.hiltbinder.BindType;
import com.paulrybitskyi.hiltbinder.keys.MapStringKey;

@MapStringKey("two")
@BindType(contributesTo = BindType.Collection.MAP)
public class Test2 implements Testable {}