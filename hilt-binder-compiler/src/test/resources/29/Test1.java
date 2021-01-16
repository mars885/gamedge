import com.paulrybitskyi.hiltbinder.BindType;
import com.paulrybitskyi.hiltbinder.keys.MapStringKey;

@MapStringKey("one")
@BindType(contributesTo = BindType.Collection.MAP)
public class Test1 implements Testable {}