{{- define "student-app.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "student-app.fullname" -}}
{{- if .Values.fullnameOverride -}}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- include "student-app.name" . -}}
{{- end -}}
{{- end -}}

{{- define "student-app.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "student-app.labels" -}}
helm.sh/chart: {{ include "student-app.chart" . }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end -}}

{{- define "student-app.selectorLabels" -}}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end -}}

{{- define "student-app.apiGatewayImage" -}}
{{- printf "%s/%s:%s" .Values.global.imageRegistry .Values.apiGateway.image.repository .Values.global.imageTag -}}
{{- end -}}

{{- define "student-app.customerApiImage" -}}
{{- printf "%s/%s:%s" .Values.global.imageRegistry .Values.customerApi.image.repository .Values.global.imageTag -}}
{{- end -}}

{{- define "student-app.postgresImage" -}}
{{- printf "%s:%s" .Values.postgres.image.repository .Values.postgres.image.tag -}}
{{- end -}}
