<#-- @ftlvariable name="" type="io.qifan.infrastructure.generator.processor.model.front.Api" -->
import { ${entityType.typeName}, Page, QueryRequest } from "@/typings";
import requestWithToken from "@/utils/request";
<#assign uncapitalizeTypeName = entityType.getUncapitalizeTypeName()>

export const query${entityType.typeName} = (query: QueryRequest<${entityType.typeName}>) => {
return requestWithToken<${"Page<"+entityType.typeName+">"}>("/${uncapitalizeTypeName}/query", "POST", query);
};

export const save${entityType.typeName} = (data: ${entityType.typeName}) => {
if (data.id) {
return requestWithToken<${'string'}>(`/${uncapitalizeTypeName}/${r'${data.id}'}/update`, "POST", data);
}
return requestWithToken<${'string'}>("/${uncapitalizeTypeName}/create", "POST", data);
};
export const find${entityType.typeName}ById = (id: string) => {
return requestWithToken<${entityType.typeName}>("/${uncapitalizeTypeName}/" + id, "GET", {});
};

export const invalid${entityType.typeName} = (id: string) => {
return requestWithToken<${'boolean'}>(`/${uncapitalizeTypeName}/${r'${id}'}/invalid`, "POST", {});
};
export const valid${entityType.typeName} = (id: string) => {
return requestWithToken<${'boolean'}>(`/${uncapitalizeTypeName}/${r'${id}'}/valid`, "POST", {});
};
export const delete${entityType.typeName} = (ids: string[]) => {
return requestWithToken<${'boolean'}>(`/${uncapitalizeTypeName}/delete`, "POST", ids);
};