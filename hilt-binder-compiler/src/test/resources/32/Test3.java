import com.paulrybitskyi.hiltbinder.BindType;
import com.paulrybitskyi.hiltbinder.keys.MapClassKey;

import javax.inject.Named;

@BindType(
    contributesTo = BindType.Collection.MAP,
    withQualifier = true
)
@MapClassKey(Test3.class)
@Named("three")
public class Test3 implements Testable {}