import com.paulrybitskyi.hiltbinder.BindType;
import com.paulrybitskyi.hiltbinder.keys.MapClassKey;

import javax.inject.Named;

@BindType(
    contributesTo = BindType.Collection.MAP,
    withQualifier = true
)
@MapClassKey(Test2.class)
@Named("two")
public class Test2 implements Testable {}