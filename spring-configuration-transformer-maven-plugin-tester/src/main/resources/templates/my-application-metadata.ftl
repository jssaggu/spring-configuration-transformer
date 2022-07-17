<html>
<head>
    <title>${title}</title>
    <style>
        h1 {
            color: darkgrey;
            font-family: verdana;
            font-size: 200%;
        }

        section, table, tr, td {
            font-family: Arial;
            font-size: 100%;
        }

        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
        }

    </style>
</head>
<body>
<h1>My own ${title}</h1>

<#if metadata.items??>
    <section>
        <table border="1">
            <tr>
                <th>Name</th>
                <th>Description</th>
                <th>Type</th>
                <th>Default</th>
                <th>Deprecation</th>
            </tr>
            <#list metadata.items as item>

                <#if item.isOfItemType(PROPERTY)>
                    <tr>
                        <td nowrap><#if item.name??>${item.name}</#if></td>
                        <td><#if item.description??>${item.description}</#if></td>
                        <td><#if item.type??>${item.type}</#if></td>
                        <td>
                            <#if item.defaultValue??>
                                <#if item.defaultValue?is_enumerable>
                                    <#list item.defaultValue as df>
                                        <dd>${df}</dd>
                                    </#list>
                                <#else>
                                    ${item.defaultValue?string}
                                </#if>
                            </#if>
                        </td>
                        <td>
                            <#if item.deprecation??>
                                <#if item.deprecation.reason??>
                                    <b>Reason: </b> ${item.deprecation.reason}<br>
                                </#if>
                                <#if item.deprecation.replacement??>
                                    <b>Replacement: </b> ${item.deprecation.replacement}
                                </#if>
                            </#if>
                        </td>
                    </tr>
                </#if>
            </#list>
        </table>
    </section>
</#if>

</body>
</html>