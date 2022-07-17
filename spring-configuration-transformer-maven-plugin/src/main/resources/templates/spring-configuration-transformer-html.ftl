<html>
<head>
    <title>${title}</title>
    <style>
        h1 {
            color: darkgrey;
            font-family: verdana,serif;
            font-size: 140%;
        }
        section,table,tr,th,td,p {
            font-family: verdana,serif;
            font-size: 0.92em;
        }
        table {
            border-collapse: collapse;
            width: auto;
            min-width: 100%;
        }
        td,th {
            border: 1px solid #ddd;
            padding: 8px;
        }
        tr:nth-child(even) {
            background-color: #f2f2f2;
        }
        tr:hover {
            background-color: #ddd;
        }
        tr td:first-child {
            white-space: nowrap;
        }
        th {
            padding-top: 12px;
            padding-bottom: 12px;
            text-align: left;
            background-color: #007CB3;
            color: white;
        }
        table caption {
            text-align: left;
            font-size: 0.8em;
            padding: 10px 0;
            color: gray;
        }
        .wrapper {
            overflow-x: scroll;
        }
    </style>
</head>
<body>
<h1>${title}</h1>

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