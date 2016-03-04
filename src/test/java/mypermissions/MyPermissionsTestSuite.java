package mypermissions;

import metest.api.BaseSuite;
import mypermissions.command.CommandTest;
import mypermissions.permissions.PermissionsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CommandTest.class,
        PermissionsTest.class
})
public class MyPermissionsTestSuite extends BaseSuite {


}
