# .Net
import clr

clr.AddReference('System.Management')
import System.Management

HKEY_LOCAL_MACHINE = 0x80000002


def read_dword(node, bit, subkey, key):
    scope_str = r"\\%s\root\cimv2" % node
    objctx = System.Management.ManagementNamedValueCollection()

    if bit in (32, 64):
        objctx.Add("__ProviderArchitecture", bit)
    elif bit == 32:
        raise Exception("Architecture not supported")

    objctx.Add("__RequiredArchitecture", True)
    scope = System.Management.ManagementScope(scope_str)
    scope.Options.Context = objctx
    path = System.Management.ManagementPath("StdRegProv")
    mc = System.Management.ManagementClass(scope, path, None)

    in_params = mc.GetMethodParameters("GetDWORDValue")
    in_params["hDefKey"] = System.UInt32(HKEY_LOCAL_MACHINE)
    in_params["sSubKeyName"] = subkey
    in_params["sValueName"] = key
    invoke_options = System.Management.InvokeMethodOptions()
    invoke_options.Context = objctx
    out_params = mc.InvokeMethod("GetDWORDValue", in_params, invoke_options)
    err_code = out_params['ReturnValue']
    if err_code != 0:
        error_msg = '\n2: Registry path not found'
        raise Exception("Error reading registry, error code: {}.{}".format(err_code, error_msg))
    else:
        return out_params['uValue']


def main():
    machine = 'localhost'
    sub_key = r"SOFTWARE\Microsoft\Windows\Windows Error Reporting"
    key = 'ServiceTimeout'
    ret = read_dword(machine, 64, sub_key, key)
    print('{} : {}'.format(key, ret))

if __name__ == '__main__':
    main()
