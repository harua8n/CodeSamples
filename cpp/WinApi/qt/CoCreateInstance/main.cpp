#include <QDebug>
#include <windows.h>

// http://stackoverflow.com/questions/2313432/how-to-retrieve-the-interface-id-of-a-com-class-so-that-it-can-be-passed-to-cocr

int main(int /*argc*/, char** /*argv[]*/)
{
    HRESULT hResult;
    CLSID ClassID;

    if(!(hResult = SUCCEEDED(::CoInitialize(NULL))))
    {
        return 1;
    }

    if(SUCCEEDED(CLSIDFromProgID(OLESTR("Scripting.FileSystemObject"), &ClassID)))
    {
        IDispatch *pDispatch;

        if(SUCCEEDED(CoCreateInstance(ClassID, NULL, CLSCTX_INPROC_SERVER,
                IID_IDispatch, (void **)&pDispatch)))
        {
            OLECHAR *sMember = const_cast<OLECHAR*>(L"FileExists");

            DISPID idFileExists;

            if(SUCCEEDED(pDispatch->GetIDsOfNames(
                    IID_NULL, &sMember, 1, LOCALE_SYSTEM_DEFAULT, &idFileExists)))
            {
                unsigned int puArgErr = 0;

                VARIANT VarResult;
                EXCEPINFO pExcepInfo;

                VariantInit(&VarResult);

                DISPPARAMS pParams;
                memset(&pParams, 0, sizeof(DISPPARAMS));
                pParams.cArgs = 1;

                VARIANT Arguments[1];
                VariantInit(&Arguments[0]);

                pParams.rgvarg = Arguments;
                pParams.cNamedArgs = 0;
                pParams.rgvarg[0].vt = VT_BSTR;
                pParams.rgvarg[0].bstrVal = SysAllocString(L"D:\\test.txt");

                hResult = pDispatch->Invoke(
                    idFileExists,
                    IID_NULL,
                    LOCALE_SYSTEM_DEFAULT,
                    DISPATCH_METHOD,
                    &pParams,
                    &VarResult,
                    &pExcepInfo,
                    &puArgErr
                );

                SysFreeString(pParams.rgvarg[0].bstrVal);

                printf("File Exists? %d\n", abs(VarResult.boolVal));
            }

            pDispatch->Release();
        }
    }

    CoUninitialize();

    return 0;
}
