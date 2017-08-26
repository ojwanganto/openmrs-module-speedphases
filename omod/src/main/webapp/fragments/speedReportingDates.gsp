<div><b>Please select reporting dates for Speed Report</b></div>
<br/>
<br/>
<br/>
<div>
    Existing Dates <br/><br/>
    <span><strong>Start Date:</strong> ${startDate}</span>&nbsp;&nbsp;
    <span><strong>End Date:</strong> ${endDate}</span>
</div>
<br/>
<br/>
<form method="post" enctype="multipart/form-data" action="${ ui.actionLink("SpeedPhasesReports", "speedReportingDates", "saveDates") }">
    <div>
        <b>Start Date: </b> ${ ui.includeFragment("kenyaui", "field/java.util.Date", [ id: "startDate", formFieldName: "startDate"]) }
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <b>End Date: </b> ${ ui.includeFragment("kenyaui", "field/java.util.Date", [ id: "endDate", formFieldName: "endDate"]) }
    </div>
    <br/>
    <br/>
    <br/>
    <span>
        <button type="submit">
            <img src="${ ui.resourceLink("kenyaui", "images/glyphs/ok.png") }" /> Save Dates
        </button>
    </span>
</form>

