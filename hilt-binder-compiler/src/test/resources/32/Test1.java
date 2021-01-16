import com.paulrybitskyi.hiltbinder.BindType;
import com.paulrybitskyi.hiltbinder.keys.MapClassKey;

import javax.inject.Named;

@BindType(
    contributesTo = BindType.Collection.MAP,
    withQualifier = true
)
@MapClassKey(Test1.class)
@Named("one")
public class Test1 implements Testable {}