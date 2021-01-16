import com.paulrybitskyi.hiltbinder.BindType;
import com.paulrybitskyi.hiltbinder.keys.MapStringKey;

@MapStringKey("three")
@BindType(contributesTo = BindType.Collection.MAP)
public class Test3 implements Testable {}