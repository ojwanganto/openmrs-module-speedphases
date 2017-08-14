<script type="text/javascript">
    $( function() {
        $( "#startDate" ).datepicker({
            changeMonth: true,
            changeYear: true
        });

        $( "#endDate" ).datepicker({
            changeMonth: true,
            changeYear: true
        });
    } );
</script>

<fieldset>
     <legend>Cohort File Upload</legend>
    <p><B>Cohort file status: </B></p>
        <div style="color: red;font-family: verdana, arial, helvetica; font-size: 16px;border: blue">
            Please set reporting start and end dates appropriately
        </div>
        <hr color="green">
        <form method="post" enctype="multipart/form-data" action="${ ui.actionLink("SpeedPhasesReports", "moduleUploadFile", "uploadFile") }">
            Start Date: <input type="text" id="startDate">
            End Date: <input type="text" id="endDate">
                <button type="submit">
                    <img src="${ ui.resourceLink("kenyaui", "images/glyphs/ok.png") }" /> Save
                </button>
            </span>
        </form>
</fieldset>