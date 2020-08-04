// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
document.addEventListener("DOMContentLoaded", getTagNamesForDisplay());
document.addEventListener("DOMContentLoaded", getCauseNamesForDisplay());
document.getElementById('charity').addEventListener('submit', function (e)
{
    e.preventDefault();
    displaySubmittedAlert()
}
);

/**
 * Gets the tags the users select to add charities
 * from the Java servlet.
 */
function getTagNamesForDisplay()
{
    fetch('/addNewCharity').then(response => response.json()).then((tagsAndCauses) =>
    {
        $(document).ready(function ()
        {
            $('.select-tag').select2(
            {
                placeholder: "Select associated category",
                data: tagsAndCauses['tags']
            }
            );
        }
        );
    }
    );
}

/**
 * Gets the tags the users select to add charities
 * from the Java servlet.
 */
function getCauseNamesForDisplay()
{
    fetch('/addNewCharity').then(response => response.json()).then((tagsAndCauses) =>
    {
        $(document).ready(function ()
        {
            $('.select-cause').select2(
            {
                placeholder: "Select associated cause",
                data: tagsAndCauses['causes']
            }
            );
        }
        );
    }
    );
}

/**
 * Displays confirmation alerts before form is
 * submitted to the Java servlet.
 */
function displaySubmittedAlert()
{
    swal(
    {
        title: "Are you sure?",
        text: "You are about to add a new charity to the database!",
        icon: "warning",
        buttons: [
            'No, cancel it!',
            'Yes, I am sure!'
        ]
    }
    ).then(function (isConfirm)
    {
        if (isConfirm)
        {
            swal(
            {
                title: 'Confirmed!',
                text: 'Charity is successfully submitted!',
                icon: 'success'
            }
            ).then(function ()
            {
                document.getElementById('charity').submit();
            }
            );
        }
        else
        {
            swal("Cancelled", ":)", "error");
        }
    }
    );
}
